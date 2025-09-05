import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
import { ShoppingCart, Loader2, Star } from "lucide-react";
import { cn } from "@/lib/utils";

// This is the one official "blueprint" for a Product. We "export" it.
export interface Product {
  id: string;
  name: string;
  image: string;
  price?: number;
  originalPrice?: number;
  rating?: number;
  isBestseller?: boolean;
}

interface ProductCardProps {
  product: Product;
  onAddToCart: (productId: string) => void;
  isLoading?: boolean;
}

export const ProductCard = ({ product, onAddToCart, isLoading }: ProductCardProps) => {
  return (
    <Card className="group bg-card border border-border rounded-xl p-4 transition-all duration-300 hover:shadow-lg hover:-translate-y-1 relative">
      {product.isBestseller && (
        <div className="absolute top-0 right-0 bg-primary text-primary-foreground text-xs font-bold px-3 py-1 rounded-bl-lg rounded-tr-lg z-10">
          Bestseller
        </div>
      )}
      <div className="aspect-[4/3] relative mb-4 overflow-hidden rounded-lg">
        <img src={product.image} alt={product.name} className="w-full h-full object-cover transition-transform duration-500 group-hover:scale-105" />
      </div>
      <div className="space-y-3">
        <div>
          <h3 className="text-lg font-semibold text-card-foreground truncate">{product.name}</h3>
          {product.rating && (
            <div className="flex items-center gap-1 mt-1">
              {Array.from({ length: 5 }, (_, i) => (
                <Star key={i} className={cn("w-4 h-4", i < Math.floor(product.rating!) ? "text-yellow-400 fill-yellow-400" : "text-gray-300")} />
              ))}
              <span className="text-xs text-muted-foreground ml-1">({product.rating.toFixed(1)})</span>
            </div>
          )}
        </div>
        <div className="flex items-center justify-between">
           <div className="flex items-baseline gap-2">
             {product.price !== undefined && (<p className="text-lg font-bold text-foreground">₹{product.price.toFixed(2)}</p>)}
             {product.originalPrice && (<p className="text-sm text-muted-foreground line-through">₹{product.originalPrice.toFixed(2)}</p>)}
           </div>
          <Button variant="commerce" size="sm" className="flex items-center gap-2" onClick={() => onAddToCart(product.id)} disabled={isLoading}>
            {isLoading ? <Loader2 className="w-4 h-4 animate-spin" /> : <ShoppingCart className="w-4 h-4" />}
            <span>{isLoading ? "" : "Add"}</span>
          </Button>
        </div>
      </div>
    </Card>
  );
};