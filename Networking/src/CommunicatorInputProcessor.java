public class CommunicatorInputProcessor{

    private boolean threadSwitch = true;
    Communicator communicator;

    public CommunicatorInputProcessor(Communicator communicator) {
        this.communicator = communicator;
    }

    public void processMessage(String message) {
        communicator.setCommunicatorMode(Communicator.CommunicatorMode.WAITING);
        try {
            if (message.matches("^Strategic Game Server Fixed .*$")) {
                System.out.println("STARTUP: " + message);
            }
            else if (message.contains("Copyright")) {
                System.out.println("STARTUP: " + message);
                communicator.setStartup(true);
            }
            else if (message.contains("MATCH")){
                //todo parse playertomove, gametype, opponent
                System.out.println("MATCH: Match found.");
                //TODO implement
            }
            else if (message.contains("YOURTURN")){
                //todo parse turnmessage
                System.out.println("MATCH: My turn.");
                //TODO implement
            }
            else if (message.contains("WIN")) {
                //TODO implement
            }
            else if (message.contains("LOSS")){
                //TODO implement
            }
            else if (message.contains("DRAW")){
                //TODO implement
            }
            else if (message.contains("CHALLENGER")){
                System.out.println("A CHALLENGER APPROACHES: " + message);
                //TODO implement
            }
            else if (message.contains("CANCELLED")){
                System.out.println("A CHALLENGE IS CANCELED: " + message);
                //TODO implement
            }
            else {
                communicator.getMessageQueue().put(message);
            }
            communicator.setCommunicatorMode(Communicator.CommunicatorMode.READY);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
