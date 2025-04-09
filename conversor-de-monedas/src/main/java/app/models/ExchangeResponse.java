package app.models;

public record ExchangeResponse(
    String result,
    String documentation,
    String terms_of_use,
    long time_last_update_unix,
    String time_last_update_utc,
    long time_next_update_unix,
    String time_next_update_utc,
    String base_code,
    String target_code,
    double conversion_rate,
    double conversion_result
) {}

