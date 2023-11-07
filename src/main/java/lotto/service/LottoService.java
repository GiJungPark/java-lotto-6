package lotto.service;

import lotto.domain.Lotto;
import lotto.domain.Winner;
import lotto.exception.ErrorCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static camp.nextstep.edu.missionutils.Randoms.pickUniqueNumbersInRange;

public class LottoService {

    private static final int START_NUMBER = 1;
    private static final int END_NUMBER = 45;
    private static final int LOTTO_NUMBER_COUNT = 6;
    private static final int THREE_MATCH = 3;
    private static final int FOUR_MATCH = 4;
    private static final int FIVE_MATCH = 5;
    private static final int SIX_MATCH = 6;
    private static final int FIRST_PLACE_PRIZE = 5000;
    private static final int SECOND_PLACE_PRIZE = 50000;
    private static final int THIRD_PLACE_PRIZE = 1500000;
    private static final int FOURTH_PLACE_PRIZE = 30000000;
    private static final int FIFTH_PLACE_PRIZE = 2000000000;

    private List<Lotto> purchaseLotto = new ArrayList<>();
    private Winner winners;
    private Lotto winLotto;
    private int bonusNumber;
    private int totalPrize;

    public void repeatPurchase(int lottoCount) {
        for (int count = 0; count < lottoCount; count++) {
            generateLotto();
        }
    }

    public List<Lotto> getPurchaseLotto() {
        return this.purchaseLotto;
    }

    private void generateLotto() {

        Lotto lotto = new Lotto(pickUniqueNumbersInRange(START_NUMBER, END_NUMBER, LOTTO_NUMBER_COUNT));

        purchaseLotto.add(lotto);
    }

    public void setWinLotto(List<Integer> winLottoNumbers) {
        winLotto = new Lotto(winLottoNumbers);
    }

    public void setBonusNumber(int bonusNumber) {

        bonusNumberDuplicateCheck(bonusNumber);

        this.bonusNumber = bonusNumber;
    }

    private void bonusNumberDuplicateCheck(int bonusNumber) {
        if (winLotto.getLotto().contains(bonusNumber)) {
            throw new IllegalArgumentException(ErrorCode.DUPLICATE_NUMBER.getMessage());
        }
    }

    public void winStatistics() {

        winners = new Winner();

        for (Lotto lotto : purchaseLotto) {

            List<Integer> notMatchNumbers = lotto.getLotto();
            notMatchNumbers.removeAll(winLotto.getLotto());

            winFirstPlace(notMatchNumbers);
            winSecondOrThirdPlace(notMatchNumbers);
            winFourthPlace(notMatchNumbers);
            winFifthPlace(notMatchNumbers);
        }

    }

    private void winFirstPlace(List<Integer> notMatchNumbers) {
        if (notMatchNumbers.size() == LOTTO_NUMBER_COUNT - SIX_MATCH) {
            winners.increase1stPlace();
        }
    }

    private void winSecondOrThirdPlace(List<Integer> notMatchNumbers) {
        if (notMatchNumbers.size() == LOTTO_NUMBER_COUNT - FIVE_MATCH) {
            checkBonusNumber(notMatchNumbers);
        }
    }

    private void checkBonusNumber(List<Integer> notMatchNumbers) {

        if (notMatchNumbers.contains(bonusNumber)) {
            winners.increase2ndPlace();
            return;
        }

        winners.increase3rdPlace();
    }

    private void winFourthPlace(List<Integer> notMatchNumbers) {
        if (notMatchNumbers.size() == LOTTO_NUMBER_COUNT - FOUR_MATCH) {
            winners.increase4thPlace();
        }
    }

    private void winFifthPlace(List<Integer> notMatchNumbers) {
        if (notMatchNumbers.size() == LOTTO_NUMBER_COUNT - THREE_MATCH) {
            winners.increase5thPlace();
        }
    }

    public List<Integer> getWinStatistics() {
        List<Integer> placeCounts = new ArrayList<>();
        placeCounts.add(getFirstPlaceCount());
        placeCounts.add(getSecondPlaceCount());
        placeCounts.add(getThirdPlaceCount());
        placeCounts.add(getFourthPlaceCount());
        placeCounts.add(getFifthPlaceCount());

        return placeCounts;
    }

    private int getFirstPlaceCount() {
        totalPrize += winners.get1stPlace() * FIRST_PLACE_PRIZE;
        return winners.get1stPlace();
    }

    private int getSecondPlaceCount() {
        totalPrize += winners.get2ndPlace() * SECOND_PLACE_PRIZE;
        return winners.get2ndPlace();
    }

    private int getThirdPlaceCount() {
        totalPrize += winners.get3rdPlace() * THIRD_PLACE_PRIZE;
        return winners.get3rdPlace();
    }

    private int getFourthPlaceCount() {
        totalPrize += winners.get4thPlace() * FOURTH_PLACE_PRIZE;
        return winners.get4thPlace();
    }

    private int getFifthPlaceCount() {
        totalPrize += winners.get5thPlace() * FIFTH_PLACE_PRIZE;
        return winners.get5thPlace();
    }

    public float getRevenue(int purchasePrice) {
        return (totalPrize / purchasePrice) * 100;
    }
}
