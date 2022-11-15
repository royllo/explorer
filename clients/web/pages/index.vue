<template>
  <!-- ============================================================================================================= -->
  <!-- Search engine screen                                                                                          -->
  <!-- ============================================================================================================= -->

  <div class="flex flex-col h-screen mt-11">

    <!-- Logo (The mx-auto should also go to the image as tailwind make the image a block element) -->
    <NuxtLink to="/">
      <img class="mx-auto"
           src="/royllo_logo_homepage.png"
           alt="Royllo logo"/>
    </NuxtLink>

    <!-- Search form -->
    <SearchAssetForm/>

  </div>

</template>
