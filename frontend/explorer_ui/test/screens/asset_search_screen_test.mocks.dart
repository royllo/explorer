// Mocks generated by Mockito 5.3.2 from annotations
// in explorer_ui/test/screens/asset_search_screen_test.dart.
// Do not manually edit this file.

// ignore_for_file: no_leading_underscores_for_library_prefixes
import 'package:explorer_ui/graphql/api/queryAssets.data.gql.dart' as _i4;
import 'package:explorer_ui/graphql/api/queryAssets.var.gql.dart' as _i5;
import 'package:ferry_exec/src/operation_request.dart' as _i2;
import 'package:ferry_exec/src/operation_response.dart' as _i3;
import 'package:mockito/mockito.dart' as _i1;

// ignore_for_file: type=lint
// ignore_for_file: avoid_redundant_argument_values
// ignore_for_file: avoid_setters_without_getters
// ignore_for_file: comment_references
// ignore_for_file: implementation_imports
// ignore_for_file: invalid_use_of_visible_for_testing_member
// ignore_for_file: prefer_const_constructors
// ignore_for_file: unnecessary_parenthesis
// ignore_for_file: camel_case_types
// ignore_for_file: subtype_of_sealed_class

class _FakeOperationRequest_0<TData, TVars> extends _i1.SmartFake
    implements _i2.OperationRequest<TData, TVars> {
  _FakeOperationRequest_0(
    Object parent,
    Invocation parentInvocation,
  ) : super(
          parent,
          parentInvocation,
        );
}

/// A class which mocks [OperationResponse].
///
/// See the documentation for Mockito's code generation for more information.
// ignore: must_be_immutable
class MockResponse extends _i1.Mock
    implements
        _i3.OperationResponse<_i4.GqueryAssetsData, _i5.GqueryAssetsVars> {
  @override
  _i2.OperationRequest<
      _i4.GqueryAssetsData, _i5.GqueryAssetsVars> get operationRequest => (super
          .noSuchMethod(
        Invocation.getter(#operationRequest),
        returnValue:
            _FakeOperationRequest_0<_i4.GqueryAssetsData, _i5.GqueryAssetsVars>(
          this,
          Invocation.getter(#operationRequest),
        ),
        returnValueForMissingStub:
            _FakeOperationRequest_0<_i4.GqueryAssetsData, _i5.GqueryAssetsVars>(
          this,
          Invocation.getter(#operationRequest),
        ),
      ) as _i2.OperationRequest<_i4.GqueryAssetsData, _i5.GqueryAssetsVars>);
  @override
  _i3.DataSource get dataSource => (super.noSuchMethod(
        Invocation.getter(#dataSource),
        returnValue: _i3.DataSource.None,
        returnValueForMissingStub: _i3.DataSource.None,
      ) as _i3.DataSource);
  @override
  bool get loading => (super.noSuchMethod(
        Invocation.getter(#loading),
        returnValue: false,
        returnValueForMissingStub: false,
      ) as bool);
  @override
  bool get hasErrors => (super.noSuchMethod(
        Invocation.getter(#hasErrors),
        returnValue: false,
        returnValueForMissingStub: false,
      ) as bool);
}