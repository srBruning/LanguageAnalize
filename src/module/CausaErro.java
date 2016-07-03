package module;

public class CausaErro {
	private int linha, colIni, colFim;
	private String msg;
	private String metadataLog;

	public CausaErro(String msg, int linha, int colIni, int colFim) {
		this(msg, linha, colIni, colFim, null);
	}

	public CausaErro(String msg, int linha, int colIni, int colFim, String metadataLog) {
		this.metadataLog = metadataLog;
		this.colFim = colFim;
		this.colIni = colIni;
		this.linha = linha;
		this.msg = msg;
	}

	public int getLinha() {
		return linha;
	}

	public void setLinha(int linha) {
		this.linha = linha;
	}

	public int getColIni() {
		return colIni;
	}

	public void setColIni(int colIni) {
		this.colIni = colIni;
	}

	public int getColFim() {
		return colFim;
	}

	public void setColFim(int colFim) {
		this.colFim = colFim;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getFormatedMessage() {
		return "[row: " + getLinha() + "| col:" + getColIni() + "-" + getColFim() + "] " + getMsg();
	}

	public String getMetadataLog() {
		return metadataLog;
	}

	public void setMetadataLog(String metadataLog) {
		this.metadataLog = metadataLog;
	}
	

}