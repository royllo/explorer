import 'package:explorer_ui/src/screens/about_screen.dart';
import 'package:explorer_ui/src/screens/asset_search_screen.dart';
import 'package:explorer_ui/src/screens/asset_view_screen.dart';
import 'package:explorer_ui/src/screens/home_screen.dart';
import 'package:go_router/go_router.dart';

/// Parameters
const String queryParameter = 'query';
const String pageParameter = 'page';
const String assetIdParameter = 'assetId';

/// Pages & path declarations
const String homeRouteName = 'home';
const String homeRoutePath = '/';
const String assetSearchRouteName = 'search';
const String assetSearchRoutePath = '/search';
const String assetViewRouteName = 'assets';
const String assetViewRoutePath = '/assets/:assetId';
const String aboutRouteName = 'about';
const String aboutRoutePath = '/about';

/// A routing package for Flutter that uses the Router API to provide a url-based API for navigating between different screens
final roylloRouter = GoRouter(
  routes: [
    // =========================================================================
    // Home page
    GoRoute(
      name: homeRouteName,
      path: homeRoutePath,
      builder: (context, state) => const HomeScreen(),
    ),
    // =========================================================================
    // Asset search page - Display results
    GoRoute(
      name: assetSearchRoutePath,
      path: assetSearchRoutePath,
      builder: (context, state) {
        // =====================================================================
        // TODO Use those values to set the query
        // On search page, we check the parameter to update the user query
        String query = state.queryParams[queryParameter] ?? '';
        int page = int.tryParse(state.queryParams[pageParameter] ?? '1') ?? 1;
        // =====================================================================

        return const AssetSearchScreen();
      },
    ),
    // =========================================================================
    // Asset view page - Display an asset
    GoRoute(
        name: assetViewRouteName,
        path: assetViewRoutePath,
        builder: (context, state) {
          // ===================================================================
          // TODO Use this value to set the asset id
          String assetId = state.params[assetIdParameter] ?? '';
          // ===================================================================

          return const AssetViewScreen();
        }),
    // =========================================================================
    // About page - Details who we are
    GoRoute(
      name: aboutRouteName,
      path: aboutRoutePath,
      builder: (context, state) => const AboutScreen(),
    ),
  ],
);
