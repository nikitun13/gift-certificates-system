package com.epam.esm.dao.impl;

import com.epam.esm.dao.SqlRequestParamParser;
import com.epam.esm.dao.exception.SymbolInterpretationException;
import com.epam.esm.util.ParamParseUtil;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SqlRequestParamParserImpl implements SqlRequestParamParser {

    private static final String FORMAT_SQL = "%s %s ?";

    private final Map<String, String> symbolVsSqlValue;

    public SqlRequestParamParserImpl() {
        symbolVsSqlValue = new HashMap<>();
        symbolVsSqlValue.put("~", "ILIKE");
        symbolVsSqlValue.put("<", "<=");
        symbolVsSqlValue.put(">", ">=");
        symbolVsSqlValue.put("", "=");
    }

    @Override
    public String toFormattedSqlString(String key) {
        String columnName = ParamParseUtil.removeOperationCharacterIfPresent(key);
        return columnName.length() == key.length()
                ? toFormattedSqlString(columnName, "")
                : toFormattedSqlString(columnName, extractOperation(key));
    }

    @Override
    public String toFormattedSqlString(String columnName, String operationSymbol) {
        return FORMAT_SQL.formatted(columnName, interpretSymbol(operationSymbol));
    }

    @Override
    public String interpretSymbol(String symbol) {
        if (symbolVsSqlValue.containsKey(symbol)) {
            return symbolVsSqlValue.get(symbol);
        } else {
            throw new SymbolInterpretationException("No such symbol: " + symbol);
        }
    }

    @Override
    public String extractOperation(String key) {
        int lastCharIndex = key.length() - 1;
        int lastCharCodePoint = key.codePointAt(lastCharIndex);
        return Character.isLetter(lastCharCodePoint)
                ? ""
                : key.substring(lastCharIndex);
    }
}
