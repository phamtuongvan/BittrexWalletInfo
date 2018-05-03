package pamobile.co.bittrexwalletinfo.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on 1/26/18.
 */

public class CryptoEvent {
    @SerializedName("caption")
    @Expose
    private String caption;
    @SerializedName("proof")
    @Expose
    private String proof;
    @SerializedName("caption_ru")
    @Expose
    private String captionRu;
    @SerializedName("proof_ru")
    @Expose
    private String proofRu;
    @SerializedName("public_date")
    @Expose
    private String publicDate;
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("end_date")
    @Expose
    private String endDate;
    @SerializedName("coin_name")
    @Expose
    private String coinName;
    @SerializedName("coin_symbol")
    @Expose
    private String coinSymbol;

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getProof() {
        return proof;
    }

    public void setProof(String proof) {
        this.proof = proof;
    }

    public String getCaptionRu() {
        return captionRu;
    }

    public void setCaptionRu(String captionRu) {
        this.captionRu = captionRu;
    }

    public String getProofRu() {
        return proofRu;
    }

    public void setProofRu(String proofRu) {
        this.proofRu = proofRu;
    }

    public String getPublicDate() {
        return publicDate;
    }

    public void setPublicDate(String publicDate) {
        this.publicDate = publicDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public String getCoinSymbol() {
        return coinSymbol;
    }

    public void setCoinSymbol(String coinSymbol) {
        this.coinSymbol = coinSymbol;
    }
}
