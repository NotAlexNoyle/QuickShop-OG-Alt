# QuickShop-OG

QuickShop-OG is a GPLv3-only shop plugin fork focused on keeping a non-AGPL QuickShop codebase available.

This repository is named `QuickShop-OG-Alt`, but the project, plugin, and built jar are `QuickShop-OG`.

## Goals

- Keep the codebase GPLv3-only
- Maintain a QuickShop-OG identity instead of QuickShop-Reremake branding
- Target Purpur `1.19.4` as the primary server runtime
- Preserve broad client compatibility in mixed-version setups such as `1.8` through `1.21.11`

## Build

Use Java 17 and Maven:

```bash
./mvnw -Pdebug package
```

## Runtime Notes

- `plugin.yml` keeps Folia support enabled because the codebase already contains region-scheduler handling
- Actual mixed-version client support depends on the proxy or protocol translation stack used by the server

## License

This project is licensed under GPLv3.
