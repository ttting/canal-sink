package me.ttting.canal.sink.elasticsearch;

import com.google.common.base.Preconditions;
import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by jiangtiteng on 2018/10/18
 */
public class DefaultFlatMessageParser implements FlatMessageParser {
    private Map<String, ESinkConfig> eSinkConfigMap;

    private static final DateTimeFormatter iso8601Formatter = ISODateTimeFormat.dateTime();

    private static final Chronology UTC_CHRONOLOGY = ISOChronology.getInstance(DateTimeZone.UTC);

    public DefaultFlatMessageParser(List<ESinkConfig> eSinkConfigs) {
        Preconditions.checkNotNull(eSinkConfigs, "esSinkConfig must not be null");
        Preconditions.checkArgument(eSinkConfigs.size() > 0, "esSinkConfig must not be null");
        eSinkConfigMap = eSinkConfigs.stream().collect(Collectors.toMap(e -> e.getDatabase() + "::" + e.getTable(), e -> e));
    }

    @Override
    public String parsePrimaryKey(FlatMessage flatMessage,  Map<String, String> data) {
        ESinkConfig eSinkConfig = eSinkConfigMap.get(configKeyForFlatMessage(flatMessage));
        String primaryKeyName = eSinkConfig.getPrimaryKeyName();
        String primaryKey = data.get(primaryKeyName);
        return primaryKey;
    }

    @Override
    public Map<String, String> parseSource(FlatMessage flatMessage, Map<String, String> data) {
        if (data != null) {
            data.put("@timestamp", iso8601Formatter.print(new DateTime(UTC_CHRONOLOGY)));
        }
        return data;
    }

    @Override
    public String parseIndex(FlatMessage flatMessage) {
        ESinkConfig eSinkConfig = eSinkConfigMap.get(configKeyForFlatMessage(flatMessage));
        return eSinkConfig.getIndex() == null ? flatMessage.getTable() : eSinkConfig.getIndex();
    }

    @Override
    public String parseType(FlatMessage flatMessage) {
        ESinkConfig eSinkConfig = eSinkConfigMap.get(configKeyForFlatMessage(flatMessage));
        return eSinkConfig.getType() == null ? flatMessage.getTable() : eSinkConfig.getType();
    }

    @Override
    public boolean isConfigured(FlatMessage flatMessage) {
        return eSinkConfigMap.get(configKeyForFlatMessage(flatMessage)) == null ? false : true;
    }

    public String configKeyForFlatMessage(FlatMessage flatMessage) {
        return flatMessage.getDatabase() + "::" + flatMessage.getTable();
    }
}
