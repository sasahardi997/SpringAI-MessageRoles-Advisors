package com.hardi.ai.messageroles.model;

import java.util.List;

public record CountryCities(
        String country,
        List<String> cities
) {
}
