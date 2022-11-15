<template>
  <!-- ============================================================================================================= -->
  <!-- Our header only has a button to register an asset                                                             -->
  <!-- ============================================================================================================= -->

  <header class="flex items-center justify-between px-4 py-4 bg-white">

    <!-- Left logo with link -->
    <NuxtLink to="/">
      <img src="/royllo_logo_horizontal_with_title.png"
           alt="Royllo Logo"/>
    </NuxtLink>

    <!-- Add asset button -->
    <NuxtLink to="/request/add"
              class="px-4 py-2 font-bold text-white rounded bg-blue-500 hover:bg-blue-700"
              external>
      Register an asset
    </NuxtLink>

  </header>

</template>