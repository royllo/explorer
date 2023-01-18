// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'asset_get_provider.dart';

// **************************************************************************
// RiverpodGenerator
// **************************************************************************

// ignore_for_file: avoid_private_typedef_functions, non_constant_identifier_names, subtype_of_sealed_class, invalid_use_of_internal_member, unused_element, constant_identifier_names, unnecessary_raw_strings, library_private_types_in_public_api

/// Copied from Dart SDK
class _SystemHash {
  _SystemHash._();

  static int combine(int hash, int value) {
    // ignore: parameter_assignments
    hash = 0x1fffffff & (hash + value);
    // ignore: parameter_assignments
    hash = 0x1fffffff & (hash + ((0x0007ffff & hash) << 10));
    return hash ^ (hash >> 6);
  }

  static int finish(int hash) {
    // ignore: parameter_assignments
    hash = 0x1fffffff & (hash + ((0x03ffffff & hash) << 3));
    // ignore: parameter_assignments
    hash = hash ^ (hash >> 11);
    return 0x1fffffff & (hash + ((0x00003fff & hash) << 15));
  }
}

String _$callAssetByAssetIdHash() =>
    r'e753722679d329b8df9f2ae0a00143a683925441';

/// This method calls the query asset.
/// FutureProvider is the equivalent of Provider but for asynchronous code.
///
/// Copied from [callAssetByAssetId].
class CallAssetByAssetIdProvider extends AutoDisposeFutureProvider<
    OperationResponse<GassetByAssetIdData, GassetByAssetIdVars>> {
  CallAssetByAssetIdProvider(
    this.value,
  ) : super(
          (ref) => callAssetByAssetId(
            ref,
            value,
          ),
          from: callAssetByAssetIdProvider,
          name: r'callAssetByAssetIdProvider',
          debugGetCreateSourceHash:
              const bool.fromEnvironment('dart.vm.product')
                  ? null
                  : _$callAssetByAssetIdHash,
        );

  final AssetByAssetIdQuery value;

  @override
  bool operator ==(Object other) {
    return other is CallAssetByAssetIdProvider && other.value == value;
  }

  @override
  int get hashCode {
    var hash = _SystemHash.combine(0, runtimeType.hashCode);
    hash = _SystemHash.combine(hash, value.hashCode);

    return _SystemHash.finish(hash);
  }
}

typedef CallAssetByAssetIdRef = AutoDisposeFutureProviderRef<
    OperationResponse<GassetByAssetIdData, GassetByAssetIdVars>>;

/// This method calls the query asset.
/// FutureProvider is the equivalent of Provider but for asynchronous code.
///
/// Copied from [callAssetByAssetId].
final callAssetByAssetIdProvider = CallAssetByAssetIdFamily();

class CallAssetByAssetIdFamily extends Family<
    AsyncValue<OperationResponse<GassetByAssetIdData, GassetByAssetIdVars>>> {
  CallAssetByAssetIdFamily();

  CallAssetByAssetIdProvider call(
    AssetByAssetIdQuery value,
  ) {
    return CallAssetByAssetIdProvider(
      value,
    );
  }

  @override
  AutoDisposeFutureProvider<
          OperationResponse<GassetByAssetIdData, GassetByAssetIdVars>>
      getProviderOverride(
    covariant CallAssetByAssetIdProvider provider,
  ) {
    return call(
      provider.value,
    );
  }

  @override
  List<ProviderOrFamily>? get allTransitiveDependencies => null;

  @override
  List<ProviderOrFamily>? get dependencies => null;

  @override
  String? get name => r'callAssetByAssetIdProvider';
}
