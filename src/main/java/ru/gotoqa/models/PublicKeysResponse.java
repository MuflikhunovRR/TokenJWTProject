package ru.gotoqa.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PublicKeysResponse {

    @ApiModelProperty(notes = "Public Key (PKCS1 RSA)")
    private String publicKey;

    @ApiModelProperty(notes = "Default Public Key (PKCS1 RSA)", required = true)
    @JsonProperty("default-key")
    private String defaultKey;
}
