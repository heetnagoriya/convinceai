import { useEffect } from "react";
import { X } from "lucide-react";
import { Button } from "@/components/ui/button";
import { cn } from "@/lib/utils";

interface NotificationPanelProps {
  isOpen: boolean;
  onClose: () => void;
  message: string;
}

export const NotificationPanel = ({ isOpen, onClose, message }: NotificationPanelProps) => {
  useEffect(() => {
    if (isOpen) {
      document.body.style.overflow = 'hidden';
    } else {
      document.body.style.overflow = 'unset';
    }
    
    return () => {
      document.body.style.overflow = 'unset';
    };
  }, [isOpen]);

  useEffect(() => {
    const handleEscape = (e: KeyboardEvent) => {
      if (e.key === 'Escape') {
        onClose();
      }
    };

    if (isOpen) {
      document.addEventListener('keydown', handleEscape);
    }

    return () => {
      document.removeEventListener('keydown', handleEscape);
    };
  }, [isOpen, onClose]);

  if (!isOpen) return null;

  return (
    <>
      {/* Backdrop */}
      <div 
        className="fixed inset-0 bg-black/20 backdrop-blur-sm z-40 animate-fade-in"
        onClick={onClose}
      />
      
      {/* Panel */}
      <div className={cn(
        "fixed right-0 top-0 h-full w-full max-w-md bg-card border-l border-border z-50 shadow-2xl",
        "transform transition-transform duration-500 ease-out",
        isOpen ? "translate-x-0" : "translate-x-full"
      )}>
        <div className="flex flex-col h-full">
          {/* Header */}
          <div className="flex items-center justify-between p-6 border-b border-border bg-primary-light/30">
            <h2 className="text-xl font-semibold text-card-foreground">ConvinceAI Notification</h2>
            <Button
              variant="ghost"
              size="icon"
              onClick={onClose}
              className="hover:bg-primary-light/50 rounded-full"
            >
              <X className="h-5 w-5" />
            </Button>
          </div>
          
          {/* Content */}
          <div className="flex-1 p-6 overflow-y-auto">
            <div className="prose prose-sm max-w-none">
              <div className="bg-commerce-warning/10 border border-commerce-warning/20 rounded-lg p-4 mb-4">
                <h3 className="text-commerce-warning font-semibold mb-2 flex items-center">
                  <span className="w-2 h-2 bg-commerce-warning rounded-full mr-2"></span>
                  Product Currently Out of Stock
                </h3>
              </div>
              
              <div className="text-card-foreground leading-relaxed whitespace-pre-wrap">
                {message}
              </div>
            </div>
          </div>
          
          {/* Footer */}
          <div className="p-6 border-t border-border bg-muted/30">
            <Button 
              variant="default" 
              className="w-full" 
              onClick={onClose}
            >
              Close Notification
            </Button>
          </div>
        </div>
      </div>
    </>
  );
};