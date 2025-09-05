import { Button } from "@/components/ui/button";
import { Card, CardContent, CardFooter, CardHeader, CardTitle } from "@/components/ui/card";
import { ShoppingCart, X } from "lucide-react";
import { Product } from "./ProductCard";

interface Recommendation {
  product: Product;
  message: string;
}

interface RecommendationModalProps {
  isOpen: boolean;
  onClose: () => void;
  recommendation: Recommendation | null;
  onAddToCart: (productId: string) => void;
}

export const RecommendationModal = ({ isOpen, onClose, recommendation, onAddToCart }: RecommendationModalProps) => {
  if (!isOpen || !recommendation) return null;

  // Split the message into parts for better formatting
  const messageParts = recommendation.message.split('*');
  const openingLine = messageParts[0];
  const bulletPoints = messageParts.slice(1, -1);
  const closingLine = messageParts[messageParts.length - 1];

  return (
    // Backdrop
    <div className="fixed inset-0 bg-black/60 backdrop-blur-sm z-40 flex items-center justify-center animate-fade-in">
      {/* Modal Card */}
      <Card className="w-[90vw] max-w-md bg-card border-border rounded-xl shadow-2xl z-50 animate-slide-up">
        <CardHeader className="flex flex-row items-start justify-between">
          <div className="space-y-1">
            <p className="text-sm font-semibold text-primary">A Smart Suggestion from ConvinceAI</p>
            <CardTitle>Your Item is Out of Stock!</CardTitle>
          </div>
          <Button variant="ghost" size="icon" onClick={onClose} className="shrink-0">
            <X className="w-4 h-4" />
          </Button>
        </CardHeader>
        <CardContent className="flex flex-col md:flex-row items-center gap-6">
          <div className="w-32 h-32 shrink-0">
            <img 
              src={recommendation.product.image} 
              alt={recommendation.product.name} 
              className="w-full h-full object-cover rounded-lg"
            />
          </div>
          <div className="text-sm text-muted-foreground space-y-2">
            <p>{openingLine}</p>
            <ul className="space-y-1 list-inside">
              {bulletPoints.map((point, index) => (
                <li key={index} className="flex items-start">
                  <span className="mr-2">{point.trim().split(' ')[0]}</span>
                  <span>{point.trim().substring(point.trim().indexOf(' ') + 1)}</span>
                </li>
              ))}
            </ul>
            <p>{closingLine}</p>
          </div>
        </CardContent>
        <CardFooter>
          <Button 
            className="w-full" 
            variant="commerce"
            onClick={() => onAddToCart(recommendation.product.id)}
          >
            <ShoppingCart className="w-4 h-4 mr-2" />
            Add "{recommendation.product.name}" to Cart
          </Button>
        </CardFooter>
      </Card>
    </div>
  );
};