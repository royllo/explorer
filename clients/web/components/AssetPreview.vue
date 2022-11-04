<template>
  <!-- ============================================================================================================= -->
  <!-- Asset preview (as displayed on search results).                                                               -->
  <!-- ============================================================================================================= -->

  <div>
      <a :href="assetId">{{assetId}}</a>
  </div>

</template>

<script setup>
  const props = defineProps({
    assetId: {
      type: String,
      required: true,
    }
  });
</script>