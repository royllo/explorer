<template>
  <!-- ============================================================================================================= -->
  <!-- Asset view (displayed on asset page).                                                                  -->
  <!-- ============================================================================================================= -->

  <!-- If asset is not found -->
  <div class="flex justify-center" v-if="data == null">Asset not found !</div>

  <!-- Asset description -->
  <div class="flex justify-center px-2" v-if="data != null">
    <div class="block p-6 rounded-lg shadow-lg bg-white w-2/3">
      <h5 class="text-gray-900 text-xl leading-tight font-medium mb-2">{{ data?.assetByAssetId?.name }}</h5>
      <hr>
      <br>
      <p class="text-gray-700 text-base mb-4 truncate md:text-clip">
        <!-- ======================================================================================================= -->
        <b>Creator</b><br>
        Username: {{ data?.assetByAssetId?.creator?.username }}<br>
        <br>
        <!-- ======================================================================================================= -->
        <b>Asset description</b><br>
        Asset id: {{ data?.assetByAssetId?.assetId }}<br>
        Asset metadata: {{ data?.assetByAssetId?.metaData }}<br>
        Output index: {{ data?.assetByAssetId?.outputIndex }}<br>
        <br>
        <!-- ======================================================================================================= -->
        <b>Bitcoin transaction output</b><br>
        Block height: {{ data?.assetByAssetId?.genesisPoint?.blockHeight }}<br>
        Transaction: {{ data?.assetByAssetId?.genesisPoint?.txId }} / {{ data?.assetByAssetId?.genesisPoint?.vout }}<br>
        ScriptPubKey: {{ data?.assetByAssetId?.genesisPoint?.scriptPubKey }}<br>
        ScriptPubKey (ASM): {{ data?.assetByAssetId?.genesisPoint?.scriptPubKeyAsm }}<br>
        scriptPubKey address: {{ data?.assetByAssetId?.genesisPoint?.scriptPubKeyAddress }}<br>
        Type: {{ data?.assetByAssetId?.genesisPoint?.scriptPubKeyType }}<br>
        value: {{ data?.assetByAssetId?.genesisPoint?.value }}<br>
      </p>
    </div>
  </div>

</template>

<script lang="ts" setup>

// =====================================================================================================================
// "assetId" is the value of the asset id - Comes from URL parameter.
const assetId = computed(() => {
  return useRoute().params.assetId;
});

// =====================================================================================================================
// Executing the graphQL query that retrieves an asset.
const query = gql`
    query assetByAssetId($assetId: ID!) {
      assetByAssetId(assetId: $assetId) {
        assetId,
        genesisPoint {
          blockHeight,
          txId,
          vout,
          scriptPubKey,
          scriptPubKeyAsm,
          scriptPubKeyType,
          scriptPubKeyAddress,
          value
        }
        creator {
          id,
          username
        }
        name,
        metaData,
        outputIndex
      }
}`;
const variables = {assetId: assetId.value};
const {data} = await useAsyncQuery(query, variables);

</script>