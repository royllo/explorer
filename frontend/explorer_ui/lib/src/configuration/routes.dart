import 'package:explorer_ui/main.dart';
import 'package:explorer_ui/src/screens/asset.dart';
import 'package:go_router/go_router.dart';

import '../screens/about.dart';
import '../screens/home.dart';
import '../screens/search.dart';

// A declarative routing package for Flutter that uses the Router API to provide
// a convenient, url-based API for navigating between different screens.
final roylloRouter = GoRouter(
  routes: [
    // =========================================================================
    // Home page
    GoRoute(
      name: "home",
      path: "/",
      builder: (context, state) => const HomeScreen(),
    ),
    // =========================================================================
    // Search page - Display results
    GoRoute(
      name: "search",
      path: '/search',
      builder: (context, state) {
        // =====================================================================
        // TODO Use those values to set the query
        // On search page, we check the parameter to update the user query
        String query = state.queryParams['query'] ?? '';
        int pageNumber = int.tryParse(state.queryParams['page'] ?? '1') ?? 1;
        // =====================================================================

        return const SearchScreen();
      },
    ),
    // =========================================================================
    // Asset page - Display an asset
    GoRoute(
        name: "assets",
        path: "/assets/:assetId",
        builder: (context, state) {
          // ===================================================================
          // TODO Use this value to set the asset id
          String assetId = state.params['assetId'] ?? '';
          // ===================================================================

          return const AssetScreen();
        }),
    // =========================================================================
    // About page - Details who we are
    GoRoute(
      name: "about",
      path: '/about',
      builder: (context, state) => const AboutScreen(),
    ),
  ],
);
