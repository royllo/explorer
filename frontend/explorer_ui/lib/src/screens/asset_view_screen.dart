import 'package:explorer_ui/src/providers/asset_get_provider.dart';
import 'package:explorer_ui/src/widgets/default_bottom_navigation_bart.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

import '../widgets/default_app_bar.dart';

// Asset page - Displays an asset.
// - Asset information
class AssetViewScreen extends ConsumerWidget {
  // Asset id
  final String assetId;

  // Constructor.
  AssetViewScreen({Key? key, required this.assetId}) : super(key: key);

  // The framework calls this method when this widget is inserted into the tree in a given BuildContext and when the dependencies of this widget change.
  // The framework replaces the subtree below this widget with the widget returned by this method
  @override
  Widget build(BuildContext context, WidgetRef ref) {
    // Update asset it and wait for the result.
    // ref.read(assetByAssetIdQueryProvider.notifier).setAssetId(assetId);
    final result = ref.watch(callAssetByAssetIdProvider);

    return Scaffold(
      // =====================================================================
      // An app bar consists of a toolbar and potentially other widgets.
      appBar: defaultRoylloAppBar(context),

      // =====================================================================
      // The page content.
      // A widget that centers its child within itself
      body: Center(
          // A widget that displays its children in a vertical array.
          child: Column(
        // X and Y alignments.
        mainAxisAlignment: MainAxisAlignment.center,
        crossAxisAlignment: CrossAxisAlignment.center,
        children: [
          // =====================================================================================================================
          // Royllo icon.
          Image.asset('assets/images/logo_large_without_text.png'),

          // =====================================================================================================================
          // Display the asset
          result.when(
            loading: () => const CircularProgressIndicator(),
            data: (result) {
              var asset = result.data?.assetByAssetId;
              if (asset == null) {
                return Text("No asset found with this asset id");
              } else {
                // ===============================================================================================================
                // Display the asset data
                return FractionallySizedBox(
                    widthFactor: 0.5,
                    child: Padding(
                        padding: const EdgeInsets.all(36.0),
                        child: Column(
                          children: [
                            // ===================================================================================================
                            // Asset description
                            Card(
                                clipBehavior: Clip.antiAlias,
                                child: Column(children: [
                                  ListTile(
                                      leading: Icon(Icons.currency_bitcoin_sharp),
                                      title: Text("${asset.name}", style: TextStyle(fontWeight: FontWeight.bold)),
                                      subtitle: RichText(
                                        text: TextSpan(
                                          children: <TextSpan>[
                                            // Asset name
                                            TextSpan(text: 'Asset name: ', style: const TextStyle(fontWeight: FontWeight.bold)),
                                            TextSpan(text: '${asset.name} \n'),
                                            // Asset id
                                            TextSpan(text: 'Asset id: ', style: const TextStyle(fontWeight: FontWeight.bold)),
                                            TextSpan(text: '${asset.assetId} \n'),
                                            // Creator
                                            TextSpan(text: 'Creator: ', style: const TextStyle(fontWeight: FontWeight.bold)),
                                            TextSpan(text: '${asset.creator.username} \n'),
                                            // Output index
                                            TextSpan(text: 'Output index: ', style: const TextStyle(fontWeight: FontWeight.bold)),
                                            TextSpan(text: '${asset.outputIndex} \n'),
                                          ],
                                        ),
                                      )),
                                ])),
                            // ===================================================================================================
                            // Genesis point
                            Card(
                                clipBehavior: Clip.antiAlias,
                                child: Column(children: [
                                  ListTile(
                                      leading: Icon(Icons.home),
                                      title: Text("Genesis point", style: TextStyle(fontWeight: FontWeight.bold)),
                                      subtitle: RichText(
                                        text: TextSpan(
                                          children: <TextSpan>[
                                            // Block height
                                            TextSpan(text: 'Block height: ', style: const TextStyle(fontWeight: FontWeight.bold)),
                                            TextSpan(text: '${asset.genesisPoint.blockHeight} \n'),
                                            // Tx id
                                            TextSpan(text: 'Tx id: ', style: const TextStyle(fontWeight: FontWeight.bold)),
                                            TextSpan(text: '${asset.genesisPoint.txId} \n'),
                                            // Vout
                                            TextSpan(text: 'Vout: ', style: const TextStyle(fontWeight: FontWeight.bold)),
                                            TextSpan(text: '${asset.genesisPoint.vout} \n'),
                                            //ScriptPubKey
                                            TextSpan(text: 'ScriptPubKey: ', style: const TextStyle(fontWeight: FontWeight.bold)),
                                            TextSpan(text: '${asset.genesisPoint.scriptPubKey} \n'),
                                            //scriptPubKeyAsm
                                            TextSpan(text: 'scriptPubKeyAsm: ', style: const TextStyle(fontWeight: FontWeight.bold)),
                                            TextSpan(text: '${asset.genesisPoint.scriptPubKeyAsm} \n'),
                                            //scriptPubKeyType
                                            TextSpan(text: 'scriptPubKeyType: ', style: const TextStyle(fontWeight: FontWeight.bold)),
                                            TextSpan(text: '${asset.genesisPoint.scriptPubKeyType} \n'),
                                            //scriptPubKeyAddress
                                            TextSpan(text: 'scriptPubKeyAddress: ', style: const TextStyle(fontWeight: FontWeight.bold)),
                                            TextSpan(text: '${asset.genesisPoint.scriptPubKeyAddress} \n'),
                                          ],
                                        ),
                                      )),
                                ])),
                          ],
                        )));

                // return Text("Asset found : '${asset.assetId} / ${asset.name}'");

                // ===============================================================================================================
              }
            },
            error: (err, stack) => Text('An error occurred: $err'),
          ),
          // =====================================================================================================================
        ],
      )),

      // =====================================================================
      // Bottom navigation bar.
      bottomNavigationBar: defaultRoylloBottomNavigationBar(context),
    );
  }
}
