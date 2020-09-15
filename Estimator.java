import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Estimator {
    private int size;
    private int number;
    private int interval;

    public static void main(String[] args)throws Exception{
        /*var est = new Estimator(10, 200, 10);
        var from = new DatagramSocket(8008);
        var toAddress = InetAddress.getByName("localhost");
        int toPort = 7007;
        est.estimate(from, toAddress, toPort);*/
        List<String> sent = new ArrayList<>(Arrays.asList("00", "01", "02", "03", "04", "05"));
        List<String> received1 = new ArrayList<>(Arrays.asList("00", "01", "01", "03", "02", "05"));
        List<String> received2 = new ArrayList<>(Arrays.asList("00", "01", "04", "05"));
        List<String> received3 = new ArrayList<>(Arrays.asList("05", "04", "03", "02", "01", "00"));

        System.out.println(Arrays.toString(Estimator.LostCounter(sent, received1)));
        System.out.println(Arrays.toString(Estimator.LostCounter(sent, received2)));
        System.out.println(Arrays.toString(Estimator.LostCounter(sent, received3)));
    }

    public Estimator(int size, int number, int interval){
        this.size = size;
        this.number = number;
        this.interval = interval;
    }

    public void estimate(DatagramSocket from, InetAddress toAddress, int toPort) throws Exception{
        List<String> sentMessages = new ArrayList<>();
        for(int i = 0; i < number; i++){
            
            var message = createZeroPaddedMessage(size, "" + i);
            var msgByte = message.getBytes();
            sentMessages.add(message);
            DatagramPacket packet = new DatagramPacket(msgByte, msgByte.length, toAddress, toPort);
            
            from.send(packet);
            System.out.print("sent " + message);
            TimeUnit.MILLISECONDS.sleep(interval);
        }
        
        System.out.println(LostCounter(sentMessages, emptySocket(from)));
        
    }

    private String createZeroPaddedMessage(int size, String originalMessage){
        StringBuilder sb = new StringBuilder();

        for(int i = originalMessage.length(); i < size; i++){
            sb.append("0");
        }
        sb.append(originalMessage);
        return sb.toString(); 
    }

    private List<String> emptySocket(DatagramSocket toEmpty) throws IOException{
        /* Empty all UDP messages from Socket.
         * A datagramsocket can hold a lot of messages,
         * by experiment we put 2000 messages in queue and none where deleted
         */
        List<String> messages = new ArrayList<>();

        toEmpty.setSoTimeout(interval);
        while(true){
            try{
                DatagramPacket response = new DatagramPacket(new byte[size], size);
                toEmpty.receive(response);
                messages.add(new String(response.getData()));
                System.out.print("Received: " + new String(response.getData()));
            } catch(SocketTimeoutException e) {
                System.out.println("Timed out!");
                break;
            }
        }

        toEmpty.setSoTimeout(0);  // Set timeout to infinite

        return messages;
    }

    public static int[] LostCounter(List<String> sent, List<String> received){
        int lose = 0;
        int dup = 0;
        int reorder = 0;
        //check if everything is good
        if(!received.equals(sent)){
            Set<String> noDup = new HashSet<>();
            //check for duplicates
            for (String msg : received){
                if (noDup.add(msg)==false) 
                dup++;
            }
            //check for lost packets
            for(String message : sent){
                if(!noDup.contains(message)){
                    lose++;
                }
            }

            //check for reoreder
            // A reordered packet is defined as on which arrives out of sequence in relation to its neighbours.
            // Since our messages are numbered, that means that if a message is higher than the left neighbour and lower than the right
            // it us ordered. Otherwise, it is reordered. 

            // check for #0 and #n-1

            for(int i = 1; i < received.size() - 1; i++){
                int left = Integer.parseInt(received.get(i-1));
                int cur = Integer.parseInt(received.get(i));
                int right = Integer.parseInt(received.get(i+1));
                
                if(!(left <= cur && cur <= right)) {
                    reorder++;
                }
            }
            
        }
        int[] tmp = new int[]{lose, dup, reorder};
        return tmp;
    }
}
