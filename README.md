# react-native-local-download

Simple download folder access for react-native

Like my projects? Support me on ko-fi!

<a href='https://ko-fi.com/W7W7X8T7W' target='_blank'><img height='42' style='border:0px;height:42px;' src='https://storage.ko-fi.com/cdn/kofi6.png?v=6' border='0' alt='Buy Me a Coffee at ko-fi.com' /></a>

The purpose of this package is as an easy method to downloading.

- Uses MediaStorage for Android Download folder access
- Uses Sharing for iOS

This package is intended to be used with other file-system packages such as `expo-file-system` or `react-native-fs`

This was not tested on lower Android SDK API's, might not working on Android 9 and below.

## Installation

```sh
npm install @vali98/react-native-fs
```

## Usage

```js
import { LocalDownload } from './NativeLocalDownload'

// Use simply add the path of the internal app resource to be copied to downloads
localDownload('/path/to/resource')

// Use a callback if you want to handle `never_ask_again` permissions on Android
localDownload('path/to/resource', () => console.log('Uh oh!'))
```

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT

---

Made with [create-react-native-library](https://github.com/callstack/react-native-builder-bob)
