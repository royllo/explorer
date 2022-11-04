<template>
  <!-- ============================================================================================================= -->
  <!-- Search engine and result screen.                                                                              -->
  <!-- ============================================================================================================= -->
  <div class="flex flex-col h-screen items-center">

    <!-- Search form -->
    <SearchAssetForm/>

    <!-- No value for the "q" query string parameter -->
    <p v-if="!q" class="mt-11">Your search must, at least, contains a character!</p>

  </div>

</template>

<script setup>
// "q" is the value search by the use.
const q = computed(() => {
  return useRoute().query.q;
});
</script>