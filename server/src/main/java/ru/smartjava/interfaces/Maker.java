package ru.smartjava.interfaces;

import ru.smartjava.server.messages.Message;

public interface Maker {

    boolean isExitCommand(Message command);

    void printMessage(Message message);

    String logIncomingMessage(Message message);

    String logOutgoingMessage(Message message, String destination);

    void printCommand(Message message);

    String connectAcceptedMessage(String nickname);

    String connectRejectedMessage(String nickname);

    String exitMessage(String nickname);

    String disconnectMessage(String nickname);

    String errorMessageString(String command);

    Message exitClientMessage(String nickName);

    Message newClientMessage(String nickName);

    Message message(String nickName, String message);

    Message connect(String nickName);

    Message clientCommand(String nickName, String userCommand);

    Message connectAccepted(String nickName);

    Message disconnect(String nickName, String command);

    Message connectRejected(String nickName);

    Message wrongMessage(String command);

    Message overSubscribe();

}
