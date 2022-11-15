<template>
  <!-- ============================================================================================================= -->
  <!-- Search engine and result screen.                                                                              -->
  <!-- ============================================================================================================= -->

  <div class="flex flex-col h-screen items-center">

    <!-- =========================================================================================================== -->
    <!-- Search form -->
    <SearchAssetForm
        :searchedValue="q"
    />

    <!-- =========================================================================================================== -->
    <!-- Search results -->
    <div class="mt-11">

      <!-- No value for the "q" query string parameter -->
      <p v-if="!q">Your search must, at least, contains a character!</p>

      <!-- No asset found -->
      <p v-if="data?.queryAssets?.length === 0">No asset found.</p>

      <!-- At least one asset was found, we create a list of it -->
      <div class="flex flex-wrap flex-row">
        <AssetCard v-for="asset in data?.queryAssets" :key="asset.assetId"
                   :assetId=asset.assetId
                   :genesisPoint=asset.genesisPoint
                   :creator=asset.creator
                   :name=asset.name
        />
      </div>

    </div>

  </div>

</template>

<script lang="ts" setup>

// =====================================================================================================================
// "q" is the value searched by the user - Usually filled in the form but can come from url parameter q.
const q = computed(() => {
  return useRoute().query.q;
});

// =====================================================================================================================
// Executing the graphQL query that search for assets from some characters.
const query = gql`
query queryAssets($value: String!) {
    queryAssets(value: $value) {
      assetId,
      genesisPoint {
        txId,
        vout
      }
      creator {
        username
      }
      name,
    }
}`;
const variables = {value: q.value?.toString().trim()};
const {data} = await useAsyncQuery(query, variables);

</script>