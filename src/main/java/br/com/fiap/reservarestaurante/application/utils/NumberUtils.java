package br.com.fiap.reservarestaurante.application.utils;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class NumberUtils {

  private static final DecimalFormat df;

  static {
    DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
    symbols.setDecimalSeparator('.');
    df = new DecimalFormat("#.0", symbols);
    df.setRoundingMode(RoundingMode.HALF_DOWN);
  }

  private NumberUtils() {
    throw new UnsupportedOperationException(
        "Esta é uma classe utilitária e não pode ser instanciada.");
  }

  public static double roundToOneDecimalPlace(double value) {
    return Double.parseDouble(df.format(value));
  }
}
