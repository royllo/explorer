import 'package:explorer_ui/main.dart';
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
        // On search page, we check the parameter to update the user query
        String q = state.queryParams['q'] ?? '';
        int pageNumberInt = int.tryParse(state.queryParams['page'] ?? '1') ?? 1;

        return SearchScreen();
      },
    ),
    // =========================================================================
    // About page - Details who we are
    GoRoute(
      name: "about",
      path: '/about',
      builder: (context, state) => const AboutScreen(),
    ),
  ],
);
