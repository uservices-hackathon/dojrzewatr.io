# Maturing service

Once enough ingredients have been sent to the maturing service it's time to brew the beer.
After some time passed we can notify the bottling service that the wort is ready.
We also update the presenting service that the current brewing process has started.

## Requirements

- Working Zookeeper

## How to run it

```
./gradlew bootRun
```

### I don't want Zikpin

Just run it in `dev` mode

```
./gradlew bootRun -Dspring.profiles.active=dev
```

## Authors

The code is ported from https://github.com/uservices-hackathon. 
The authors of the code are:
- Marcin Grzejszczak (marcingrzejszczak)
- Tomasz Szymanski (szimano)