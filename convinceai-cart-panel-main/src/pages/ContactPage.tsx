import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import { Footer } from "@/components/Footer";
import { Link } from "react-router-dom";
import { Mail, Phone, MapPin } from "lucide-react";

// A simple logo component to reuse
const ConvinceAiLogo = () => (
    <svg width="28" height="28" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" className="text-primary">
      <path d="M12 2C6.477 2 2 6.477 2 12s4.477 10 10 10 10-4.477 10-10S17.523 2 12 2zm0 18c-4.411 0-8-3.589-8-8s3.589-8 8-8 8 3.589 8 8-3.589 8-8 8z" fill="currentColor"/>
      <path d="M12 7c-2.761 0-5 2.239-5 5s2.239 5 5 5 5-2.239 5-5-2.239-5-5-5zm0 8c-1.654 0-3-1.346-3-3s1.346-3 3-3 3 1.346 3 3-1.346 3-3 3z" fill="currentColor"/>
    </svg>
);

const ContactPage = () => {
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
              <a href="/#deals" className="text-muted-foreground hover:text-primary transition-colors">Deals</a>
              <a href="#" className="text-muted-foreground hover:text-primary transition-colors">New Arrivals</a>
              <Link to="/contact" className="text-muted-foreground hover:text-primary transition-colors">Contact</Link>
            </nav>
          </div>
        </div>
      </header>

      <main className="flex-grow container mx-auto px-4 py-16">
        <div className="max-w-4xl mx-auto">
            <div className="text-center mb-12">
              <h1 className="text-4xl md:text-5xl font-bold text-foreground mb-4">Get in Touch</h1>
              <p className="text-lg text-muted-foreground">We'd love to hear from you! Please fill out the form below or contact us directly.</p>
            </div>

            <div className="grid md:grid-cols-2 gap-12 items-start">
                {/* Contact Form */}
                <div className="space-y-4">
                    <Input placeholder="Your Name" />
                    <Input type="email" placeholder="Your Email" />
                    <Textarea placeholder="Your Message" className="min-h-[150px]" />
                    <Button variant="commerce" className="w-full">Send Message</Button>
                </div>

                {/* Contact Details */}
                <div className="space-y-6 bg-card p-8 rounded-lg border">
                    <h3 className="text-2xl font-semibold text-foreground">Contact Information</h3>
                    <div className="space-y-4 text-muted-foreground">
                        <div className="flex items-center gap-4">
                            <MapPin className="w-5 h-5 text-primary"/>
                            <span>123 Snack Lane, Ahmedabad, Gujarat, India</span>
                        </div>
                         <div className="flex items-center gap-4">
                            <Mail className="w-5 h-5 text-primary"/>
                            <span>contact@convinceai.com</span>
                        </div>
                         <div className="flex items-center gap-4">
                            <Phone className="w-5 h-5 text-primary"/>
                            <span>+91 12345 67890</span>
                        </div>
                    </div>
                     <p className="text-xs pt-4 border-t">We typically respond within 24 business hours. For urgent inquiries, please call us.</p>
                </div>
            </div>
        </div>
      </main>

      <Footer />
    </div>
  );
};

export default ContactPage;