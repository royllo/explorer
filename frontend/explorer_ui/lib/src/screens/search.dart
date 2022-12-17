import 'package:explorer_ui/main.dart';
import 'package:explorer_ui/src/widgets/default_app_bar.dart';
import 'package:explorer_ui/src/widgets/default_bottom_navigation_bart.dart';
import 'package:explorer_ui/src/widgets/search_form.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:number_paginator/number_paginator.dart';

import '../services/AssetService.dart';

// Search page - Displays results.
// - Search field
// - Results
// - Page
class SearchScreen extends ConsumerWidget {
  // Constructor.
  // The framework calls this method when this widget is inserted into the tree in a given BuildContext and when the dependencies of this widget change.
  // The framework replaces the subtree below this widget with the widget returned by this method
  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final result = ref.watch(assetQueryProvider);

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
        mainAxisAlignment: MainAxisAlignment.start,
        crossAxisAlignment: CrossAxisAlignment.center,
        children: [
          // ===================================================================
          // Search box in a container to add a margin
          Container(
            margin: const EdgeInsets.all(20),
            // Search text field.
            child: SearchForm(),
          ),
          // ===================================================================
          // Results
          FractionallySizedBox(
              widthFactor: 0.5,
              child: Column(
                children: [
                  // ===================================================================
                  // Display each coin
                  result.when(
                    loading: () => const CircularProgressIndicator(),
                    data: (result) {
                      var builder =
                          result.data?.queryAssets?.content?.toBuilder();
                      return ListView.builder(
                        scrollDirection: Axis.vertical,
                        shrinkWrap: true,
                        itemCount: builder?.length,
                        itemBuilder: (context, index) {
                          return ListTile(
                            leading: const CircleAvatar(),
                            title: Text("${builder?[index]?.name}"),
                            subtitle:
                                Text('Asset id: ${builder?[index]?.assetId}'),
                            trailing: const Icon(Icons.help),
                          );
                        },
                      );
                    },
                    error: (err, stack) => Text('An error occurred: $err'),
                  ),
                  // ===================================================================
                  // Display pages
                  result.when(
                    loading: () => const Text(""),
                    data: (result) {
                      var totalPages = result.data?.queryAssets?.totalPages;
                      if (totalPages != null) {
                        // We display the pages
                        return NumberPaginator(
                          initialPage: ref.watch(searchRequestProvider).pageNumber - 1,
                          numberPages: totalPages,
                          onPageChange: (int index) {
                            ref
                                .watch(searchRequestProvider.notifier)
                                .update((state) => SearchRequest(state.query, pageNumber: index + 1));
                          },
                        );
                      } else {
                        return const Text("");
                      }
                    },
                    error: (error, stackTrace) => const Text(""),
                  )
                ],
              )),
        ],
      )),

      // =====================================================================
      // Bottom navigation bar.
      bottomNavigationBar: defaultRoylloBottomNavigationBar(context),
    );
  }
}
