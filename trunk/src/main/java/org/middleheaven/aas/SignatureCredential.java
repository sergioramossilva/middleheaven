package org.middleheaven.aas;


public class SignatureCredential implements RefreshableCredential {


	private static final long serialVersionUID = 7009219326038799093L;

	Signature signature;
	public SignatureCredential(Signature signature){
		this.signature = signature;
	}
	
	@Override
	public boolean isCurrent() {
		return signature.isValid();
	}

	@Override
	public void refresh() throws RefreshFailedException {
		try{
			signature.refresh();
		} catch (SignatureException e){
			throw new RefreshFailedException();
		}
	}

	
	public boolean equals(Object other){
		return other instanceof SignatureCredential && ((SignatureCredential)other).signature.equals(this.signature);
	}
	
	public int hashCode(){
		return signature.hashCode();
	}
}
