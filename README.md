# Interval-based Task on Foreground Service

- Using count-down timer on Foreground Service.
- Mainly useful for small interval-based work where interval is less than 15 minutes and app needs to stay in Foreground
- Otherwise it's better to use WorkManager with Foreground Service.

Improvements
- As Count-down Timer uses it's own `handler`, maybe it's better to make a custom Count-down Timer with a reference to `handler`, so that more functionality can be added
- There may be other improvements, let me know here

References:
- https://developer.android.com/guide/components/services
- https://developer.android.com/guide/components/foreground-services
- https://stackoverflow.com/questions/10032003/how-to-make-a-countdown-timer-in-android
