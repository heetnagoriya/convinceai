import { ProductCard, Product } from "@/components/ProductCard";

interface DealsSectionProps {
  dealProducts: Product[];
  onAddToCart: (productId: string) => void;
  loadingProductId: string | null;
}

export const DealsSection = ({ dealProducts, onAddToCart, loadingProductId }: DealsSectionProps) => {
  return (
    <div className="bg-background py-16">
      <div className="container mx-auto px-4">
        <div className="text-center mb-12">
          <h2 className="text-3xl font-bold text-foreground mb-2">
            Limited Time Deals
          </h2>
          <p className="text-md text-muted-foreground max-w-2xl mx-auto">
            Don't miss out on these special offers on your favourite snacks!
          </p>
        </div>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-8 max-w-3xl mx-auto">
          {dealProducts.map((product) => (
            <ProductCard
              key={product.id}
              product={product}
              onAddToCart={onAddToCart}
              isLoading={loadingProductId === product.id}
            />
          ))}
        </div>
      </div>
    </div>
  );
};
