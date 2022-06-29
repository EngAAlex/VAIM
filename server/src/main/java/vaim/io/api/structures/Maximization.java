package vaim.io.api.structures;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Maximization
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-03-02T11:05:55.322Z[GMT]")


public class Maximization   {
  @JsonProperty("readable")
  private String readable = null;

  @JsonProperty("key")
  private String key = null;

  public Maximization name(String name) {
    this.readable = name;
    return this;
  }
  
  public Maximization(String name, String code) {
	  this.readable = name;
	  this.key = code;
  }

  /**
   * Get name
   * @return name
   **/
  @ApiModelProperty(value = "")
  
    public String getName() {
    return readable;
  }

  public void setName(String name) {
    this.readable = name;
  }

  public Maximization code(String code) {
    this.key = code;
    return this;
  }

  /**
   * Get code
   * @return code
   **/
  @ApiModelProperty(value = "")
  
    public String getCode() {
    return key;
  }

  public void setCode(String code) {
    this.key = code;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Maximization maximization = (Maximization) o;
    return Objects.equals(this.readable, maximization.readable) &&
        Objects.equals(this.key, maximization.key);
  }

  @Override
  public int hashCode() {
    return Objects.hash(readable, key);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Maximization {\n");
    
    sb.append("    name: ").append(toIndentedString(readable)).append("\n");
    sb.append("    code: ").append(toIndentedString(key)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
