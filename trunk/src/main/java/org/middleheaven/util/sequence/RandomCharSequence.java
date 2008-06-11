/*
 * Created on 2006/08/12
 *
 */
package org.middleheaven.util.sequence;

import java.util.Random;

public class RandomCharSequence implements RandomSequence<CharSequence>{

    /**
     * @author  Sergio M. M. Taborda 
     */
    public enum TemplateType {
        NUMERIC_ONLY, ALFA_NUMERIC , ALFA_ONLY
    }
    private static CharSequence getModeTemplate(TemplateType mode){
        StringBuilder builder;
        switch (mode){
        case NUMERIC_ONLY:
            return "1234567890";
        case ALFA_ONLY:
            builder = new StringBuilder("abcdefghijklmnopqrstuvwxyz");
            builder.append(builder.toString().toUpperCase());
            return builder;
        case ALFA_NUMERIC:
            builder = new StringBuilder("abcdefghijklmnopqrstuvwxyz");
            builder.append(builder.toString().toUpperCase());
            builder.append("1234567890");
            return builder;
        default :
            throw new IllegalArgumentException (mode.toString() + " is not a recognized TemplateType");    
        }
    }
    
    public RandomCharSequence newSequence(int length){
        return new RandomCharSequence(length, "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
    }
    
    public RandomCharSequence newSequence(int length, TemplateType templateType ){
        return new RandomCharSequence(length,getModeTemplate(templateType));
    }
    
    public RandomCharSequence newSequence(int length, CharSequence template  ){
        return new RandomCharSequence(length,template);
    }
    
    int length;
    CharSequence template;
    Random random;
    protected RandomCharSequence(int length,CharSequence template){
        this.length = length;
        this.template = template;
        this.random = new Random();
    }
    
    @Override
    public SequenceToken<CharSequence> next() {
        final StringBuilder buffer = new StringBuilder();
        int templateLength = this.template.length();
        while (buffer.length () < length) {
            char c = template.charAt(random.nextInt(templateLength));
            buffer.append(c);
        }
        return new DefaultToken<CharSequence>(buffer);

    }

}
