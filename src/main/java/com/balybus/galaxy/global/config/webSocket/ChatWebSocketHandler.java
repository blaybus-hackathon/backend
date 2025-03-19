//package com.balybus.galaxy.global.config.webSocket;
//
//import org.springframework.web.socket.TextMessage;
//import org.springframework.web.socket.WebSocketHandler;
//import org.springframework.web.socket.WebSocketSession;
//
//public class ChatWebSocketHandler implements WebSocketHandler {
//
//    @Override
//    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//        // WebSocket 연결이 성립되었을 때 호출됨
//        System.out.println("WebSocket 연결이 성립되었습니다. 세션 ID: " + session.getId());
//        // 예: 사용자에게 연결 성공 메시지 보내기
//        session.sendMessage(new TextMessage("Welcome to the chat!"));
//    }
//
//    @Override
//    public void handleMessage(WebSocketSession session, org.springframework.web.socket.WebSocketMessage<?> message) throws Exception {
//        // 클라이언트로부터 메시지를 받았을 때 호출됨
//        System.out.println("Received message: " + message.getPayload());
//
//        // 받은 메시지를 처리하고, 다른 클라이언트에게 메시지 전달 (예: 브로드캐스트)
//        // 예: 클라이언트에게 응답 보내기
//        session.sendMessage(new TextMessage("Message received: " + message.getPayload()));
//    }
//
//    @Override
//    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
//        // WebSocket 연결에 오류가 발생했을 때 호출됨
//        System.out.println("Error occurred: " + exception.getMessage());
//    }
//
//    @Override
//    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus closeStatus) throws Exception {
//        // WebSocket 연결이 종료되었을 때 호출됨
//        System.out.println("WebSocket 연결이 종료되었습니다. 세션 ID: " + session.getId());
//    }
//
//    @Override
//    public boolean supportsPartialMessages() {
//        // WebSocket이 부분 메시지를 지원하는지 여부를 반환 (일반적으로 false)
//        return false;
//    }
//}
