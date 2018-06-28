package net.xinhong.oceanmonitor.message;

public class MessageProducerFactory {

    public static DataMessageProducer createMessageProducer(String type) {
        if (type.toLowerCase().equals("gfs.down")) {
            return GFSMessageProducer.getInstance();
        }
        return null;
    }


}
