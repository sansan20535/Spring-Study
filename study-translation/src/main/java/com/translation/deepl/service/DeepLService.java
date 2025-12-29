package com.translation.deepl.service;

import com.deepl.api.DeepLClient;
import com.deepl.api.DeepLException;
import com.deepl.api.TextResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DeepLService {

    @Value("${deepl.api-key}")
    private String apiKey;

    public TextResult translation(String originalText, String sourceLanguage, String targetLanguage) throws DeepLException, InterruptedException {
        DeepLClient deepLClient = new DeepLClient(apiKey);

        return deepLClient.translateText(originalText, sourceLanguage, targetLanguage);
    }
}
