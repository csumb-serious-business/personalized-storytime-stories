package storytime.common.templatefill;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * represents a String with placeholders for identifiers
 */
public class TemplatedString {
  private final String contents;
  private final Wrapper wrapper;
  private final Set<String> identifiers;

  public TemplatedString(String s) {
    this(s, new Wrapper());
  }

  public TemplatedString(String s, Wrapper w) {
    this.contents = s;
    this.wrapper = w;
    this.identifiers = captureIdentifiers(contents, wrapper);

  }

  public static Set<String> captureIdentifiers(String contents, Wrapper wrapper) {
    String regex = wrapper.wrap("(.*?)");
    Pattern p = Pattern.compile(regex);

    // extract & sort the set of identifiers by finding all regex matches
    Set<String> set = new HashSet<>(
        List.of(p.matcher(contents).results().map(MatchResult::group).toArray(String[]::new)));

    // pull identifier out of wrapping & return
    return set.stream().map(wrapper::unwrap).collect(Collectors.toSet());
  }

  public String fill(Map<String, String> map) {
    // based on https://stackoverflow.com/a/36173777
    Function<String, String> combiner = map.entrySet().stream().reduce(Function.identity(),
        (f, e) -> s -> f.apply(s).replaceAll(wrapper.wrap(e.getKey()), e.getValue()),
        Function::andThen);

    return combiner.apply(contents);
  }

  public String getContents() {
    return contents;
  }


  public Set<String> getIdentifiers() {
    return identifiers;
  }
}
