import { Button } from "@/components/ui/button";

// The component now accepts an onClick prop
interface HeroSectionProps {
  onExploreClick: () => void;
}

export const HeroSection = ({ onExploreClick }: HeroSectionProps) => {
  return (
    <div className="bg-gradient-to-r from-gray-100 to-gray-200 dark:from-gray-800 dark:to-gray-900">
      <div className="container mx-auto px-4 py-16 md:py-24 text-center">
        <h1 className="text-4xl md:text-6xl font-bold text-foreground mb-4 animate-fade-in">
          Your Favourite Snacks, Delivered Fast
        </h1>
        <p className="text-lg text-muted-foreground max-w-2xl mx-auto mb-8 animate-fade-in animation-delay-300">
          From classic chips to gourmet chocolates, get all your cravings delivered to your doorstep with ConvinceAI's smart recommendations.
        </p>
        <Button 
          size="lg" 
          variant="commerce" 
          className="animate-fade-in animation-delay-600"
          onClick={onExploreClick} // This now calls the function
        >
          Explore All Snacks
        </Button>
      </div>
    </div>
  );
};