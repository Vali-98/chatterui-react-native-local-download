import type { TurboModule } from 'react-native';
import { TurboModuleRegistry } from 'react-native';

export interface Spec extends TurboModule {
  localDownload(uri: string): Promise<void>
}

export default TurboModuleRegistry.getEnforcing<Spec>('ReactNativeLocalDownload');
