package ru.gotoqa.models;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class PublicKeyResponse {

    @ApiModelProperty(notes = "Public Key (PKCS1 RSA)", required = true)
    private String publicKey;

    @Default
    @ApiModelProperty(notes = "Today's date", required = true)
    private Date today = new Date();

    @Default
    @ApiModelProperty(notes = "Good wishes")
    private String wishes = "Eva, Merry Xmass!!";
}
