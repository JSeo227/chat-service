package com.example.chat.chat_service.global.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseApi<T> {

    // 상태코드
    private Integer status;

    //성공 여부
    private boolean success;

    //성공 or 실패 메시지
    private String message;

    //성공 데이터
    private T data;

    /**
     * API error return
     * @param <T>
     * @param data - 반환 데이터
     * @return
     */
    public static <T> ResponseApi<T> success(T data) {
        return ResponseApi.<T>builder()
                .success(true)
                .status(200)
                .message("success")
                .data(data)
                .build();
    }

    /**
     * API error return
     * @param <T>
     * @param message - 성공 메세지
     * @param data - 반환 데이터
     * @return
     */
    public static <T> ResponseApi<T> success(String message, T data) {
        return ResponseApi.<T>builder()
                .success(true)
                .status(200)
                .message(message)
                .data(data)
                .build();
    }

    /**
     * API error return
     * @param <T>
     * @param data - 반환 데이터
     * @return
     */
    public static <T> ResponseApi<T> error(T data) {
        return error(data, 500);
    }

    /**
     * API error return
     * @param <T>
     * @param data - 반환 데이터
     * @param status - 상태코드
     * @return
     */
    public static <T> ResponseApi<T> error(T data, Integer status) {
        return ResponseApi.<T>builder()
                .success(false)
                .status(status)
                .message("false")
                .data(data)
                .build();
    }

    /**
     * API error return
     * @param <T>
     * @param message - 오류메세지
     * @return
     */
    public static <T> ResponseApi<T> error(String message) {
        return error(message, 500);
    }

    /**
     * API error return
     * @param <T>
     * @param message - 오류메세지
     * @param status - 상태코드
     * @return
     */
    public static <T> ResponseApi<T> error(String message, Integer status) {
        return ResponseApi.<T>builder()
                .success(false)
                .status(status)
                .message(message)
                .data(null)
                .build();
    }
}
