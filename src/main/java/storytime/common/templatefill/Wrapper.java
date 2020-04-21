package storytime.common.templatefill;

import java.util.regex.Pattern;

/**
 * Wraps an identifier in a {@link TemplatedString}
 */
public class Wrapper {
  private final String prefix;
  private final String suffix;


  public Wrapper() {
    this("${", "}");
  }

  public Wrapper(String prefix, String suffix) {
    this.prefix = Pattern.quote(prefix);
    this.suffix = Pattern.quote(suffix);
  }

  public String wrap(String s) {
    return this.prefix + s + this.suffix;
  }

  public String unwrap(String s) {
    return s.replaceAll(prefix, "").replaceAll(suffix, "");
  }

}
