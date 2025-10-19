## Environment Variables

Before running the bot, you need to set the following environment variables:

- `TELEGRAM_BOT_TOKEN` — **required** — your bot’s token from BotFather.
- `TELEGRAM_LOG_ID` — **optional** — the chat ID where the bot will send log messages.

### Examples

**Linux/macOS (bash/zsh):**
```bash
export TELEGRAM_BOT_TOKEN="123456:ABC-DEF1234ghIkl-zyx57W2v1u123ew11"
export TELEGRAM_LOG_ID="987654321"
```

### Translations

You can add a new .json file in `/resources/langs` and in the `loadTranslations()` method to add a new language

### Requirements

- `Redis Server` — **required** — you must have a redis server to which you can connect in `RedisRepository` class.
