package com.mcpdev.mcpdev.service;

import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcpdev.mcpdev.error.BadRequestException;
import com.mcpdev.mcpdev.error.InternalServerException;
import com.mcpdev.mcpdev.request.JsonRpcRequest;
import com.mcpdev.mcpdev.request.JsonRpcRequestT;
import com.mcpdev.mcpdev.response.JsonRpcResponse;

@Service
public class JsonRpcService {
    protected static final String SUPPORTED_JSON_RPC = "2.0";

    protected final Logger _log = LoggerFactory.getLogger(this.getClass());
    protected final ObjectMapper _serializer = new ObjectMapper();

    public JsonRpcService() {
        _serializer.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        _serializer.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
    }

    void validateJsonRpc(JsonRpcRequest req)
            throws BadRequestException {
        if (!SUPPORTED_JSON_RPC.equals(req.jsonrpc())) {
            _log.warn("unsupported jsonrpc {}", req.jsonrpc());
            throw new BadRequestException();
        }
    }

    protected JsonRpcRequest deserializeRequest(byte[] raw)
            throws BadRequestException, InternalServerException {
        try {
            final var req = _serializer.readValue(raw, JsonRpcRequest.class);
            validateJsonRpc(req);
            return req;
        } catch (DatabindException e) {
            _log.warn(e.toString());
            throw new BadRequestException();
        } catch (Exception e) {
            _log.warn(e.toString());
            throw new InternalServerException();
        }
    }

    protected <T> T deserializeT(byte[] raw, Class<T> type)
            throws InternalServerException {
        try {
            final var gType = _serializer.getTypeFactory()
                    .constructParametricType(JsonRpcRequestT.class, type);
            final JsonRpcRequestT<T> reqT = _serializer.readValue(raw, gType);
            return reqT.params();
        } catch (Exception e) {
            _log.warn(e.toString());
            throw new InternalServerException();
        }
    }

    protected byte[] Ok() {
        return "{\"jsonrpc\": \"2.0\"}".getBytes(StandardCharsets.UTF_8);
    }

    protected <R, E> byte[] serializeResponse(long id, R result, @Nullable E error)
            throws InternalServerException {
        try {
            final var res = new JsonRpcResponse<>(
                    SUPPORTED_JSON_RPC,
                    id,
                    result,
                    error);
            final var rawRes = _serializer.writeValueAsBytes(res);
            return rawRes;
        } catch (Exception e) {
            _log.warn(e.toString());
            throw new InternalServerException();
        }
    }
}
