import { useState, useRef } from "react";
import { Link } from "react-router-dom";
import { ProductCard, Product } from "@/components/ProductCard";
import { RecommendationModal } from "@/components/RecommendationModal";
import { HeroSection } from "@/components/HeroSection";
import { Footer } from "@/components/Footer";
import { useApi } from "@/hooks/useApi";
import { useToast } from "@/hooks/use-toast";

import pringlesImage from "@/assets/pringles.jpg";
import laysImage from "@/assets/lays.jpg";
import oreoImage from "@/assets/oreo.jpg";
import cokeImage from "@/assets/coke.jpg";
import dairyMilkImage from "@/assets/dairymilk.jpg";
import kitkatImage from "@/assets/kitkat.jpg";
import goodDayImage from "@/assets/goodday.jpg";
import pepsiImage from "@/assets/pepsi.jpg";

interface Recommendation {
  product: Product;
  message: string;
}

const ConvinceAiLogo = () => (
    <svg width="28" height="28" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" className="text-primary">
      <path d="M12 2C6.477 2 2 6.477 2 12s4.477 10 10 10 10-4.477 10-10S17.523 2 12 2zm0 18c-4.411 0-8-3.589-8-8s3.589-8 8-8 8 3.589 8 8-3.589 8-8 8z" fill="currentColor"/>
      <path d="M12 7c-2.761 0-5 2.239-5 5s2.239 5 5 5 5-2.239 5-5-2.239-5-5-5zm0 8c-1.654 0-3-1.346-3-3s1.346-3 3-3 3 1.346 3 3-1.346 3-3 3z" fill="currentColor"/>
    </svg>
);

const Index = () => {
  const [loadingProductId, setLoadingProductId] = useState<string | null>(null);
  const [modalData, setModalData] = useState<Recommendation | null>(null);
  const { addToCart } = useApi();
  const { toast } = useToast();

  const productsRef = useRef<HTMLDivElement>(null);
  const dealsRef = useRef<HTMLDivElement>(null);

  // We add the 'recommendedProductId' to our frontend data to make smarter recommendations
  const allProducts: (Product & { recommendedProductId?: string })[] = [
    { id: "e59443ab-6710-48ad-91fc-d3348aee7b5e", name: "Cadbury Dairy Milk", image: dairyMilkImage, price: 45, originalPrice: 50, rating: 4.9, isBestseller: true, recommendedProductId: "54021ba4-5529-4414-8f07-a8f754d8b354" }, // Recommends KitKat
    { id: "b1a4e3d1-62d6-40d0-8e11-0e3f5670cb77", name: "Good Day", image: goodDayImage, price: 15, originalPrice: 20, rating: 4.3, isBestseller: false },
    { id: "c2ede069-4277-46de-ac2d-39a3d778ef00", name: "Pringles Original", image: pringlesImage, price: 100, rating: 4.5, isBestseller: true, recommendedProductId: "abdd1d0c-097a-4b57-b43e-7aa04e042030" }, // Recommends Lays
    { id: "abdd1d0c-097a-4b57-b43e-7aa04e042030", name: "Lays Classic", image: laysImage, price: 50, rating: 4.7, isBestseller: true },
    { id: "205956c7-a3af-41a2-b2de-65861bc4106b", name: "Oreo Cookies", image: oreoImage, price: 40, rating: 4.8, isBestseller: false, recommendedProductId: "b1a4e3d1-62d6-40d0-8e11-0e3f5670cb77" }, // Recommends Good Day
    { id: "54021ba4-5529-4414-8f07-a8f754d8b354", name: "KitKat", image: kitkatImage, price: 25, rating: 4.6, isBestseller: false },
    { id: "13dbb963-9816-407d-a4b2-ff089772397f", name: "Coca-Cola Can", image: cokeImage, price: 35, rating: 4.7, isBestseller: false, recommendedProductId: "caabdf2f-a207-4bee-84bd-31095dbf8885" }, // Recommends Pepsi
    { id: "caabdf2f-a207-4bee-84bd-31095dbf8885", name: "Pepsi", image: pepsiImage, price: 20, rating: 4.5, isBestseller: false }
  ];

  const dealProducts = allProducts.filter(p => p.originalPrice);

  const handleAddToCart = async (productId: string) => {
    setLoadingProductId(productId);
    const result = await addToCart(productId);
    setLoadingProductId(null);
    
    // This logic now correctly uses the simple response format
    if (result.success) {
      if (result.isLongMessage) {
        // This is the corrected logic to find the paired recommendation
        const originalProduct = allProducts.find(p => p.id === productId);
        const recommendedProduct = allProducts.find(p => p.id === originalProduct?.recommendedProductId);

        if (recommendedProduct) {
          setModalData({
            product: recommendedProduct,
            message: result.message,
          });
        } else {
          // Fallback if no specific recommendation is found in the frontend list
          toast({ title: "Out of Stock", description: "This item is out of stock.", variant: "destructive" });
        }
      } else {
        alert(result.message);
      }
    } else {
      toast({ title: "Error", description: result.message, variant: "destructive" });
    }
  };

  const handleModalAddToCart = (productId: string) => {
    alert(`Added a recommended item to your cart!`);
    setModalData(null);
  };
  
  const scrollTo = (ref: React.RefObject<HTMLDivElement>) => {
      ref.current?.scrollIntoView({ behavior: 'smooth' });
  };

  return (
    <div className="min-h-screen bg-background flex flex-col">
      <header className="border-b border-border bg-card/50 backdrop-blur-sm sticky top-0 z-30">
        <div className="container mx-auto px-4 py-4">
          <div className="flex items-center justify-between">
            <Link to="/" className="flex items-center gap-2">
              <ConvinceAiLogo />
              <h1 className="text-2xl font-bold bg-gradient-to-r from-primary to-primary-hover bg-clip-text text-transparent">
                ConvinceAI
              </h1>
            </Link>
            <nav className="hidden md:flex items-center space-x-6 text-sm font-medium">
              <Link to="/" className="text-foreground hover:text-primary transition-colors">Home</Link>
              <a href="#deals" onClick={() => scrollTo(dealsRef)} className="text-muted-foreground hover:text-primary transition-colors">Deals</a>
            </nav>
          </div>
        </div>
      </header>

      <div className="flex-grow">
        <HeroSection onExploreClick={() => scrollTo(productsRef)} />

        <main className="container mx-auto px-4 py-16">
          <div className="max-w-6xl mx-auto">
            
            <div ref={dealsRef} className="text-center mb-12 scroll-mt-20">
                <h2 className="text-3xl font-bold text-foreground mb-2">Today's Hot Deals 🔥</h2>
                <p className="text-md text-muted-foreground max-w-2xl mx-auto">
                    Grab these amazing snacks at a discounted price!
                </p>
            </div>
            <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6 mb-20">
              {dealProducts.map((product) => (
                <ProductCard
                  key={product.id}
                  product={product}
                  onAddToCart={handleAddToCart}
                  isLoading={loadingProductId === product.id}
                />
              ))}
            </div>

            <div ref={productsRef} className="text-center mb-12 scroll-mt-20">
              <h2 className="text-3xl font-bold text-foreground mb-2">
                All Products
              </h2>
              <p className="text-md text-muted-foreground max-w-2xl mx-auto">
                Discover our carefully curated selection of premium snacks.
              </p>
            </div>
            <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
              {allProducts.map((product) => (
                <ProductCard
                  key={product.id}
                  product={product}
                  onAddToCart={handleAddToCart}
                  isLoading={loadingProductId === product.id}
                />
              ))}
            </div>

          </div>
        </main>
      </div>

      <Footer />

      <RecommendationModal
        isOpen={!!modalData}
        onClose={() => setModalData(null)}
        recommendation={modalData}
        onAddToCart={handleModalAddToCart}
      />
    </div>
  );
};

export default Index;