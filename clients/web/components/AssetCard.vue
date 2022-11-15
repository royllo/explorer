<template>
  <!-- ============================================================================================================= -->
  <!-- Asset preview (displayed on search results).                                                                  -->
  <!-- ============================================================================================================= -->

  <div class="flex justify-center px-2 py-2">
    <div class="block p-6 rounded-lg shadow-lg bg-white max-w-sm">
      <h5 class="text-gray-900 text-xl leading-tight font-medium mb-2">{{ name }}</h5>
      <hr>
      <br>
      <p class="text-gray-700 text-base mb-4 truncate md:text-clip">
        <b>Creator</b>: {{ creator.username }}<br>
        <br>
        <b>Asset id</b>: {{ assetId }}<br>
        <b>Genesis point</b>: {{ genesisPoint.txId }}-{{ genesisPoint.vout }}<br>
        <br>
      </p>
      <NuxtLink :to="`/asset/${assetId}`"
                external
                class="inline-block px-6 py-2.5 bg-blue-600 text-white font-medium text-xs leading-tight uppercase rounded shadow-md hover:bg-blue-700 hover:shadow-lg focus:bg-blue-700 focus:shadow-lg focus:outline-none focus:ring-0 active:bg-blue-800 active:shadow-lg transition duration-150 ease-in-out">
        View asset
      </NuxtLink>
    </div>
  </div>

</template>

<script setup>
const props = defineProps({
  // TODO fix types
  // The asset ID that uniquely identifies the asset.
  assetId: {
    type: String,
    required: true,
  },
  // The first outpoint of the transaction that created the asset (txid:vout).
  genesisPoint: {
    type: String,
    required: true,
  },
  // The asset creator.
  creator: {
    type: String,
    required: true,
  },
  // The name of the asset
  name: {
    type: String,
    required: true,
  },
});

</script>