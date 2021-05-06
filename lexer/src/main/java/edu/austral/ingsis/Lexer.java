package edu.austral.ingsis;

import edu.austral.ingsis.tokens.Token;
import edu.austral.ingsis.tokens.TokenType;
import edu.austral.ingsis.util.Keywords;
import edu.austral.ingsis.util.StateType;
import edu.austral.ingsis.util.WordsToken;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Lexer {

  private final Map<String, TokenType> keyWords = Keywords.getKeyword1_1();
  private final Map<String, TokenType> wordsKeyWords = WordsToken.getWords1_1();
  private String accum = "";
  private StateType state = StateType.EMPTY;
  private List<Token> tokens = new ArrayList<>();
  private Integer index = 0;

  public List<Token> lex(List<String> text) {
    clear();
    Integer lineNumber = 0;
    for (String s : text) {
      List<Character> line = s.chars().mapToObj(e -> (char) e).collect(Collectors.toList());
      for (Character c : line) {
        tokenize(c, lineNumber);
      }
      this.index = 0;
      lineNumber++;
    }
    return tokens;
  }

  private void tokenize(Character c, Integer lineNumber) {
    if (c.toString().equals("\"")) {
      if (accum.isEmpty()) {
        state = StateType.STRING;
        accum += "\"";
      } else if (state.equals(StateType.STRING)) {
        accum += "\"";
        createToken(TokenType.LITERAL, lineNumber);
      }
    } else if (state.equals(StateType.STRING)) accum += c.toString();
    else if (isCompound(c) && state.equals(StateType.EMPTY)) {
      state = StateType.COMPOUND_KEYWORD;
      accum += c.toString();
    } else if (isCompound(c)
        && !accum.isEmpty()
        && (state.equals(StateType.NUMBER) || state.equals(StateType.IS_LETTER))) {
      if (state.equals(StateType.NUMBER)) createToken(TokenType.LITERAL, lineNumber);
      else createToken(TokenType.IDENTIFIER, lineNumber);
      accum += c.toString();
      state = StateType.COMPOUND_KEYWORD;
    } else if (state.equals(StateType.COMPOUND_KEYWORD) && isCompound(c)) {
      accum += c.toString();
    } else if (accum.isEmpty() && isNumber(c)) {
      accum += c.toString();
      state = StateType.NUMBER;
    } else if (isNumber(c) && state.equals(StateType.COMPOUND_KEYWORD)) {
      createToken(wordsKeyWords.get(accum), lineNumber);
      state = StateType.NUMBER;
      accum += c;
    } else if (state.equals(StateType.NUMBER) && isNumber(c)) accum += c.toString();
    else if (accum.isEmpty() && isLetter(c)) {
      state = StateType.IS_LETTER;
      accum += c.toString();
    } else if (isLetter(c) && state.equals(StateType.IS_LETTER)) {
      accum += c.toString();

    } else if (c.toString().equals(" ") && accumIsWordKeyword()) {
      createToken(wordsKeyWords.get(accum), lineNumber);
    } else if (isKeyword(c) && !accum.isEmpty()) {
      if (state.equals(StateType.NUMBER) && !isNumber(c)) {
        createToken(TokenType.LITERAL, lineNumber);
        accum += c.toString();
        createToken(keyWords.get(c.toString()), lineNumber);
      } else if (state.equals(StateType.IS_LETTER) || state.equals(StateType.EMPTY)) {
        if (accumIsWordKeyword()) createToken(wordsKeyWords.get(accum), lineNumber);
        else createToken(TokenType.IDENTIFIER, lineNumber);
        accum += c.toString();
        createToken(keyWords.get(c.toString()), lineNumber);
      }

    } else if (isKeyword(c) && accum.isEmpty()) {
      accum += c.toString();
      createToken(keyWords.get(c.toString()), lineNumber);
    } else if (state.equals(StateType.NUMBER) && c.toString().equals(".")) {
      accum += c.toString();
    }
  }

  private void createToken(TokenType tokenType, Integer line) {
    tokens.add(Token.builder().value(accum).type(tokenType).index(index).line(line).build());
    this.index += 1;
    this.state = StateType.EMPTY;
    accum = "";
  }

  private boolean isKeyword(Character c) {
    return keyWords.containsKey(c.toString());
  }

  private boolean isCompound(Character c) {
    return c.toString().equals("=") || c.toString().equals("<") || c.toString().equals(">");
  }

  private boolean accumIsKeyboard() {
    return keyWords.containsKey(accum);
  }

  private boolean isLetter(Character c) {
    return Character.isLetter(c);
  }

  private boolean isNumber(Character c) {
    return Character.isDigit(c);
  }

  private boolean accumIsWordKeyword() {
    return wordsKeyWords.containsKey(accum);
  }

  private void clear() {
    accum = "";
    state = StateType.EMPTY;
    tokens.clear();
    index = 0;
  }
}
