package nl.easthome.gameserver.networking.communication;

public class CommunicatorInputProcessor{

    private boolean threadSwitch = true;
    private Communicator communicator;

    public CommunicatorInputProcessor(Communicator communicator) {
        this.communicator = communicator;
    }

    public void processMessage(String message) {
        communicator.setCommunicatorMode(Communicator.CommunicatorMode.WAITING);
        try {
            if (message.matches("^Strategic Game Server Fixed .*$")) {
                println("STARTUP > " + message);
            }
            else if (message.contains("Copyright")) {
                println("STARTUP > " + message);
                communicator.setStartup(true);
            }
            else if (message.contains("MATCH")){
                //TODO parse playertomove, gametype, opponent
                println("MATCH > Match Found.");
                //TODO create new game.
                //TODO implement
            }
            else if (message.contains("YOURTURN")){
                //TODO parse turnmessage
                println("MATCH > My Turn.");
                //TODO implement
            }

            else if (message.contains("MOVE")){
                //TODO parse player, move, details
                println("MATCH > A Move Has Been Executed: " + message);
                //TODO implement
            }
            else if (message.contains("WIN")) {
                println("MATCH > Match has resulted in a win.");
                //TODO implement
            }
            else if (message.contains("LOSS")){
                println("MATCH > Match has resulted in a loss.");
                //TODO implement
            }
            else if (message.contains("DRAW")){
                println("MATCH > Match has resulted in a draw.");
                //TODO implement
            }
            else if (message.contains("CHALLENGER")){
                println("MATCH > Approaching challenger: " + message);
                //TODO implement
            }
            else if (message.contains("CANCELLED")){
                println("MATCH > Challenge cancelled: " + message);
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

    private void println(String message){
        System.out.println("[COMMUNICATOR] = " + message);
    }

}
