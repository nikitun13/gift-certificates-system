package com.epam.esm.dao;

import com.epam.esm.exception.SymbolInterpretationException;

/**
 * Describes interface of a parser that
 * parses request parameters due to conventions.
 */
public interface SqlRequestParamParser {

    /**
     * Parses request and formats it to SQL string.
     *
     * @param key request with operation symbol.
     * @return formatted SQL string.
     * @throws SymbolInterpretationException if parsed {@code operation symbol} is unknown.
     */
    String toFormattedSqlString(String key);

    /**
     * Formats parsed request.
     *
     * @param columnName      column name to be formatted.
     * @param operationSymbol symbol to be interpreted for formatting.
     * @return formatted SQL string.
     * @throws SymbolInterpretationException if {@code operation symbol} is unknown.
     */
    String toFormattedSqlString(String columnName, String operationSymbol);

    /**
     * Interprets symbol to SQL string value.
     *
     * @param symbol symbol to be interpreted.
     * @return SQL string value.
     * @throws SymbolInterpretationException if symbol is unknown.
     */
    String interpretSymbol(String symbol);

    /**
     * Extracts operation symbol from the request.
     *
     * @param key request to be parsed.
     * @return operation symbol.
     */
    String extractOperation(String key);
}
