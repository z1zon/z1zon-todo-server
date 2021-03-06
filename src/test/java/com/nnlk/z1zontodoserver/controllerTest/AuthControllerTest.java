package com.nnlk.z1zontodoserver.controllerTest;

import com.google.common.base.Splitter;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;


public class AuthControllerTest {


    @Test
    public void parseTest() {

        String data = "access_token=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&scope=&token_type=bearer";
        Map<String,String> map = new HashMap<>();
        Map<String,String> queryParameters = Splitter
                .on("&")
                .withKeyValueSeparator("=")
                .split(data);

        assert ("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".equals(queryParameters.get("access_token")));
        assert ("".equals(queryParameters.get("scope")));
        assert ("bearer".equals(queryParameters.get("token_type")));
    }
}
