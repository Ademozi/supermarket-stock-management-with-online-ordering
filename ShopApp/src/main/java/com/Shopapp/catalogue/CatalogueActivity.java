public class CatalogueActivity extends AppCompatActivity {
    private CatalogueService catalogueService;
    private RecyclerView recyclerView;
    private ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogue);

        catalogueService = new CatalogueService();
        recyclerView = findViewById(R.id.productRecycler);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        List<Product> products = catalogueService.browseCatalogue();
        adapter = new ProductAdapter(products);
        recyclerView.setAdapter(adapter);
    }
    adapter = new ProductAdapter(products, product -> {
    Intent intent = new Intent(CatalogueActivity.this, ProductDetailActivity.class);
    intent.putExtra("product", product);
    startActivity(intent);
});

}
