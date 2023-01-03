import 'package:explorer_ui/src/providers/asset_get_provider.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_test/flutter_test.dart';

const String validAssetId = "421ea534aac791b7c3a77e46f29cbf812db1a15667ce50d3632e2ba5cadbc6a6";

final assetByAssetIdQueryNotifier = StateProvider((ref) => AssetByAssetIdQueryNotifier());

void main() {
  test('AssetByAssetIdQuery provider behavior', () {
    final container = ProviderContainer();

    // Initial state
    var initialState = container.read(assetByAssetIdQueryNotifier.notifier).state.getQuery();
    assert(initialState.isEmpty());
    assert(initialState.assetId == "");

    // Invalid asset ids
    expect(() => container.read(assetByAssetIdQueryNotifier.notifier).state.setAssetId("a"), throwsAssertionError);
    expect(() => container.read(assetByAssetIdQueryNotifier.notifier).state.setAssetId("aa"), throwsAssertionError);
    expect(() => container.read(assetByAssetIdQueryNotifier.notifier).state.setAssetId("aaa"), throwsAssertionError);

    // Valid asset id
    container.read(assetByAssetIdQueryNotifier.notifier).state.setAssetId(validAssetId);
    assert(container.read(assetByAssetIdQueryNotifier.notifier).state.getQuery().assetId == validAssetId);
  });

  test('AssetByAssetIdQuery class behavior', () {
    // With assetId set to null, it won't compile:
    // The argument type 'Null' can't be assigned to the parameter type 'String'
    // AssetByAssetIdQuery() or AssetByAssetIdQuery(null);

    // Invalid asset id used for the constructor
    expect(() => AssetByAssetIdQuery(""), throwsAssertionError);
    expect(() => AssetByAssetIdQuery("0"), throwsAssertionError);
    expect(() => AssetByAssetIdQuery("AA"), throwsAssertionError);

    // Valid asset Id
    var ValidAssetByAssetIdQuery = AssetByAssetIdQuery(validAssetId);
    assert(ValidAssetByAssetIdQuery.assetId == validAssetId);
  });
}